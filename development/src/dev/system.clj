(ns dev.system
  "we keep vars to store the base to start and components to start
  in order to avoid them erased when reloading the dev.core namespace.")

;; You can use the convenience setters from the dev.core namespace
(comment
  (require '[dev.core :as dev])
  (dev/set-base-to-start :data-api)
  (dev/set-components-to-start :mysql))

(def base-to-start :data-api)

(def components-to-start nil)