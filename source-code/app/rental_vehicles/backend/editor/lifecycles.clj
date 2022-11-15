
(ns app.rental-vehicles.backend.editor.lifecycles
    (:require [engines.item-editor.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-editor/init-editor! :rental-vehicles.editor
                                              {:base-route      "/@app-home/rental-vehicles"
                                               :collection-name "rental_vehicles"
                                               :handler-key     :rental-vehicles.editor
                                               :item-namespace  :vehicle
                                               :on-route        [:rental-vehicles.editor/load!]
                                               :route-title     :rental-vehicles}]})
