(ns project.data.test-setup)

(defn teardown [_project-name]
  (let [f (requiring-resolve 'beop.resources.test-system/stop)]
    (f)))
