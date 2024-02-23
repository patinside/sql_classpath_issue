(ns beop.sql.queries
  (:require [clojure.java.io :as io]
            [hugsql.core :as hsql]
            [next.jdbc :as jdbc]
            [hugsql.adapter.next-jdbc :as next-adapter]
            [taoensso.timbre :as log]))

;; Set the adapter
(hsql/set-adapter! (next-adapter/hugsql-adapter-next-jdbc))

(defn initialize-test-databases [deps]
  (let [participations-fns (hsql/map-of-db-fns (io/resource "sql/participations.sql"))
        conn (jdbc/get-datasource {:dbtype "mysql"
                                   :user "root"
                                   :dbname nil
                                   :password "secret"})
        participations-writer (-> deps :mysql :participations-conn :writer :datasource)]
    (log/info "create database")
    ((:fn (:create-database participations-fns)) conn)
    (log/info "grant user")
    ((:fn (:grant-user participations-fns)) conn {:user "user"})
    (log/info "create participations table")
    ((:fn (:create-participations-table participations-fns)) participations-writer)))

(comment
  (def create-participations-fn (:fn (:create-participations-table (hsql/map-of-db-fns (io/resource "sql/participations.sql")))))
  (def grant-user (:fn (:grant-user (hsql/map-of-db-fns (io/resource "sql/participations.sql")))))
  (def conn (jdbc/get-datasource {:dbtype "mysql"
                                  :user "root"
                                  :dbname "participations"
                                  :password "secret"}))
  (grant-user conn {:user "user"})
  (create-participations-fn conn)


  (jdbc/execute! conn
                 ["drop table participations"]
                 #_["show tables"]))