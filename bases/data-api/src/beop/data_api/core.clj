(ns beop.data-api.core
  (:require  [com.stuartsierra.component :as component]
             [beop.sql.interface :as sql]))

(defn app-system []
  (let [config {:mysql {:dbtype "mysql"
                        :reader-host "mysql"
                        :writer-host "mysql"
                        :port 3306
                        :db ""
                        :user "user"
                        :password "secret"
                        :useSSL false
                        :mode nil
                        :batch-size 10000}}]


    (component/system-map
     :config config
     :mysql (sql/new-mysql-component (:mysql config) ["participations"]))))
