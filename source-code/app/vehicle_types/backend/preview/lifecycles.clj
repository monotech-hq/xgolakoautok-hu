
(ns app.vehicle-types.backend.preview.lifecycles
    (:require [engines.item-preview.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-preview/init-preview! :vehicle-types.preview
                                                {:collection-name "vehicle_types"
                                                 :handler-key     :vehicle-types.preview
                                                 :item-namespace  :type}]})
