(ns beop.sql.core
  (:require [clojure.set :as set]
            [com.stuartsierra.component :as component]
            [next.jdbc.connection :as connection]
            [taoensso.timbre :as log])
  (:import (com.zaxxer.hikari HikariDataSource)))

(defn as-reader-on
  ([config]
   (as-reader-on config nil))
  ([config db]
   (connection/->pool
    HikariDataSource
    {:jdbcUrl (connection/jdbc-url config)})))

(defn as-writer-on
  ([config]
   (as-writer-on config nil))
  ([config db]
   (connection/->pool
    HikariDataSource
    (-> config
        (assoc :jdbcUrl (connection/jdbc-url (select-keys config [:dbtype :dbname :useSSL :allowLoadLocalInfile])))
        (set/rename-keys {:user :username})))))

(defrecord Mysql [db-spec databases]
  component/Lifecycle

  (start [this]
    (log/info (format "Start mysql connection for databases %s" databases))
    (let [db-spec (assoc db-spec :allowLoadLocalInfile true)]
      (reduce (fn [acc database]
                (let [db-spec (assoc db-spec :dbname database)]
                  (assoc acc (keyword (str database "-conn"))
                         {:reader {:datasource (as-reader-on (set/rename-keys db-spec {:reader-host :host}))}
                          :writer {:datasource (as-writer-on (set/rename-keys db-spec {:writer-host :host}))}})))
              this
              databases)))

  (stop [this]
    (log/info "Stop mysql connections")
    (if (not-empty this)
      (do
        (doseq [database databases
                :let [conn (keyword (str database "-conn"))]
                :when (contains? this conn)
                :let [reader (get-in this [conn :reader :datasource])
                      writer (get-in this [conn :writer :datasource])]]
          (.close reader)
          (.close writer))
        {})
      this)))

(defn new-mysql-component [mysql-config databases]
  (->Mysql mysql-config databases))

