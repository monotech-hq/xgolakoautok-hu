
(ns app.website-content.backend.installer
    (:require [app.website-content.backend.handler.config :as handler.config]
              [io.api                                     :as io]
              [plugins.git.api                            :as git]
              [x.core.api                                 :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- installer
  []
  (git/ignore!         handler.config/WEBSITE-CONTENT-FILEPATH "app.website-content")
  (io/create-edn-file! handler.config/WEBSITE-CONTENT-FILEPATH))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-installer! ::installer installer)
