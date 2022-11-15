
(ns app.clients.backend.viewer.lifecycles
    (:require [engines.item-viewer.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-viewer/init-viewer! :clients.viewer
                                              {:base-route      "/@app-home/clients"
                                               :collection-name "clients"
                                               :handler-key     :clients.viewer
                                               :item-namespace  :client
                                               :on-route        [:clients.viewer/load-viewer!]
                                               :route-title     :clients}]})
