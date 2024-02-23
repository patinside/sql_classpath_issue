(ns dev.core
  (:require [beop.data-api.core :as data-api-core]
            [beop.sql.interface]
            [beop.export-api.core :as export-api-core]
            [dev.system]
            [com.stuartsierra.component :as component]
            [next.jdbc :as jdbc]))

(defn set-base-to-start [base]
  (alter-var-root #'dev.system/base-to-start (constantly base)))

(defn set-components-to-start [& ks]
  (alter-var-root #'dev.system/components-to-start (constantly (into [] ks))))

(defn app-system []
  (case dev.system/base-to-start
    :data-api (data-api-core/app-system)
    :export-api (export-api-core/app-system)))

;; A Var containing an object representing the application under
;;         development.
(defonce deps nil)


(defn init []
  (alter-var-root #'deps (constantly (app-system))))

(defn start []
  (alter-var-root #'deps #(component/start-system % (seq dev.system/components-to-start)))
  (keys deps))

(defn stop []
  (alter-var-root #'deps #(component/stop-system %)))

(defn go []
  (init)
  (start)
  [dev.system/base-to-start "ready"])

(defn reset []
  (stop)
  (go))


(comment
  (set-base-to-start :data-api)
  (set-components-to-start :mysql)
  (reset)
  (require '[next.jdbc :as jdbc])
  (jdbc/execute! (-> deps :mysql :participations-conn :reader :datasource) ["SELECT * FROM participations"])
  )