
(ns app.website-config.backend.installer
    (:require [app.website-config.backend.handler.config :as handler.config]
              [io.api                                    :as io]
              [plugins.git.api                           :as git]
              [x.core.api                                :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- installer
  []
  (git/ignore!         handler.config/WEBSITE-CONFIG-FILEPATH "app.website-config")
  (io/create-edn-file! handler.config/WEBSITE-CONFIG-FILEPATH))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-installer! ::installer installer)
