
(ns app.storage.backend.media-preview.lifecycles
    (:require [engines.item-preview.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-preview/init-preview! :storage.media-preview
                                                {:collection-name "storage"
                                                 :handler-key     :storage.media-preview
                                                 :item-namespace  :media}]})
