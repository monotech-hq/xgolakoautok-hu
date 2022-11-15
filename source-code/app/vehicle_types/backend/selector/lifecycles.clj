
(ns app.vehicle-types.backend.selector.lifecycles
    (:require [engines.item-lister.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :vehicle-types.selector
                                              {:collection-name "vehicle_types"
                                               :handler-key     :vehicle-types.lister
                                               :item-namespace  :type}]})
