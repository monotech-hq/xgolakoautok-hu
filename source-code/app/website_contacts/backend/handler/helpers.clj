
(ns app.website-contacts.backend.handler.helpers
    (:require [app.website-contacts.backend.handler.config :as handler.config]
              [io.api                                      :as io]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-website-contacts
  ; @return (map)
  []
  (io/read-edn-file handler.config/WEBSITE-CONTACTS-FILEPATH))
