
(ns app.website-post.backend.installer
    (:require [app.website-post.backend.handler.config :as handler.config]
              [io.api                                  :as io]
              [plugins.git.api                         :as git]
              [x.core.api                              :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- installer
  []
  (git/ignore!         handler.config/WEBSITE-POST-FILEPATH "app.website-post")
  (io/create-edn-file! handler.config/WEBSITE-POST-FILEPATH))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-installer! ::installer installer)
