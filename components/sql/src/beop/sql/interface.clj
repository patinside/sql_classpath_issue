(ns beop.sql.interface
  (:require [beop.sql.core :as core]
            [beop.sql.queries :as queries]))

(defn new-mysql-component [config databases]
  (core/new-mysql-component config databases))

(defn initialize-test-databases [deps]
  (queries/initialize-test-databases deps))