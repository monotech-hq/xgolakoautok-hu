
(ns app.storage.backend.media-selector.lifecycles
    (:require [engines.item-browser.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-browser/init-browser! :storage.media-selector
                                                {:collection-name "storage"
                                                 :handler-key     :storage.media-browser
                                                 :item-namespace  :media}]})
