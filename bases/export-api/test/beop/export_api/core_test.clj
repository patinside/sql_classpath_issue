(ns beop.export-api.core-test
  (:require [beop.resources.test-system :as test-system]
            [camel-snake-kebab.core :refer [->snake_case]]
            [clojure.test :as test :refer [deftest is use-fixtures]]
            [honey.sql :as sql]
            [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]))

(def a-participation {:id "1"
                      :content_account "aaaaaaaaaaaaaaaaaaaaaaaa"
                      :widget_account "aaaaaaaaaaaaaaaaaaaaaaaa"
                      :wrapper_id "cccccccccccccccccccccccc"
                      :viewer_account "aaaaaaaaaaaaaaaaaaaaaaa1"
                      :form_entry "a-form"
                      :optins_entries "an-optin"})

(defn insert-data-in-participations []
  (let [mysql-participations-conn (-> test-system/deps :mysql :participations-conn :writer :datasource)]
    (jdbc/execute! mysql-participations-conn (sql/format {:insert-into :participations
                                                          :values [a-participation]}))))

(defn empty-participations []
  (let [mysql-participations-conn (-> test-system/deps :mysql :participations-conn :writer :datasource)]
    (jdbc/execute! mysql-participations-conn ["TRUNCATE TABLE participations"])))

(defn participation-data-fixture [f]
  (insert-data-in-participations)
  (f)
  (empty-participations))

(use-fixtures :once
  test-system/with-test-system
  participation-data-fixture)

(deftest dummy-test
  (let [mysql-participations-conn (-> test-system/deps :mysql :participations-conn :writer :datasource)
        participation-in-mysql (jdbc/execute! mysql-participations-conn
                                              (sql/format {:select :*
                                                           :from :participations})
                                              {:builder-fn rs/as-unqualified-modified-maps
                                               :label-fn ->snake_case})]
    (is (= [a-participation] participation-in-mysql))))
