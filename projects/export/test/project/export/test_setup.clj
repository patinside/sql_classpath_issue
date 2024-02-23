(ns project.export.test-setup)
(defn teardown [_]
  (let [f (requiring-resolve 'beop.resources.test-system/stop)]
    (f)))
