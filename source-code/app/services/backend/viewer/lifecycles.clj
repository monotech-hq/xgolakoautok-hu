
(ns app.services.backend.viewer.lifecycles
    (:require [engines.item-viewer.api]
              [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-viewer/init-viewer! :services.viewer
                                              {:base-route      "/@app-home/services"
                                               :collection-name "services"
                                               :handler-key     :services.viewer
                                               :item-namespace  :service
                                               :on-route        [:services.viewer/load-viewer!]
                                               :route-title     :services}]})
