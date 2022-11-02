
(ns app.models.backend.preview.lifecycles
    (:require [engines.item-preview.api]
              [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-preview/init-preview! :models.preview
                                                {:collection-name "models"
                                                 :handler-key     :models.preview
                                                 :item-namespace  :model}]})
