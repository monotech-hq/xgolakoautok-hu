
(ns app.vehicle-types.backend.editor.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-editor/init-editor! :vehicle-types.editor
                                              {:base-route      "/@app-home/vehicle-models/:model-id/types"
                                               :collection-name "vehicle_types"
                                               :handler-key     :vehicle-types.editor
                                               :item-namespace  :type
                                               :on-route        [:vehicle-types.editor/load-editor!]
                                               :route-title     :vehicle-types}]})
