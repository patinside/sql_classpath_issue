(ns beop.resources.test-system
  (:require
   [beop.data-api.core :as data-api-core]
   [beop.sql.interface :as beop-sql]
   [com.stuartsierra.component :as component]))

(defonce deps nil)

(defn app-system []
  (data-api-core/app-system))

(defn setup-mysql []
  (println "Setup mysql...")
  (beop-sql/initialize-test-databases deps)
  (println "Setup mysql OK"))

(defn go []
  (when-not deps
    (alter-var-root #'deps (constantly (app-system)))
    (alter-var-root #'deps component/start-system)
    (setup-mysql))
  (keys deps))

(defn stop []
  (when (some? deps) (alter-var-root #'deps component/stop-system)))

(defn reset []
  (stop)
  (alter-var-root #'deps (constantly (app-system)))
  (alter-var-root #'deps component/start-system))

(defn with-test-system [f]
  (go)
  (f))

(comment
  (go)
  )
