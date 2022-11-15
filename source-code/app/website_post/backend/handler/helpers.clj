
(ns app.website-post.backend.handler.helpers
    (:require [app.website-post.backend.handler.config :as handler.config]
              [io.api                                  :as io]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-website-post
  ; @return (map)
  []
  (io/read-edn-file handler.config/WEBSITE-POST-FILEPATH))
