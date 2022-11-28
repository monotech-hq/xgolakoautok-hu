
(ns app.website-contacts.backend.installer
    (:require [app.website-contacts.backend.handler.config :as handler.config]
              [io.api                                      :as io]
              [plugins.git.api                             :as git]
              [x.core.api                                  :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- installer
  []
  (git/ignore!         handler.config/WEBSITE-CONTACTS-FILEPATH "app.website-contacts")
  (io/create-edn-file! handler.config/WEBSITE-CONTACTS-FILEPATH))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-installer! ::installer installer)
