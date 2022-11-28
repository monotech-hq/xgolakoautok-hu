
(ns app.price-quotes.backend.installer
    (:require [app.price-quotes.backend.handler.config :as handler.config]
              [io.api                                  :as io]
              [plugins.git.api                         :as git]
              [x.core.api                              :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- installer
  []
  (git/ignore!         handler.config/PRICE-QUOTE-CONFIG-FILEPATH "app.price-quotes")
  (io/create-edn-file! handler.config/PRICE-QUOTE-CONFIG-FILEPATH))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-installer! ::installer installer)
