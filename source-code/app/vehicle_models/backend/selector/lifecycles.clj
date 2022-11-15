
(ns app.vehicle-models.backend.selector.lifecycles
    (:require [engines.item-lister.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :vehicle-models.selector
                                              {:collection-name "vehicle_models"
                                               :handler-key     :vehicle-models.lister
                                               :item-namespace  :model}]})
