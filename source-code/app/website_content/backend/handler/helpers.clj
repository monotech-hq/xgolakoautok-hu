
(ns app.website-content.backend.handler.helpers
    (:require [app.website-content.backend.handler.config :as handler.config]
              [io.api                                     :as io]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-website-content
  ; @return (map)
  []
  (io/read-edn-file handler.config/WEBSITE-CONTENT-FILEPATH))
