
(ns project.ui.backend.main
    (:require [app.website-config.backend.api    :as website-config]
              [project.ui.backend.loading-screen :as loading-screen]
              [x.ui.api                          :as x.ui]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (map) request
  [request]
  (let [website-config (website-config/get-website-config)
        loading-screen (loading-screen/view request)]
       (x.ui/html (x.ui/head request website-config)
                  (x.ui/body request {:loading-screen loading-screen}))))
