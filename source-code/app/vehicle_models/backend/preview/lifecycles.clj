
(ns app.vehicle-models.backend.preview.lifecycles
    (:require [engines.item-preview.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-preview/init-preview! :vehicle-models.preview
                                                {:collection-name "vehicle_models"
                                                 :handler-key     :vehicle-models.preview
                                                 :item-namespace  :model}]})
