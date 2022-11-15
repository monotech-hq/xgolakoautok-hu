
(ns app.clients.backend.lister.lifecycles
    (:require [engines.item-lister.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :clients.lister
                                              {:base-route      "/@app-home/clients"
                                               :collection-name "clients"
                                               :handler-key     :clients.lister
                                               :item-namespace  :client
                                               :on-route        [:clients.lister/load-lister!]
                                               :route-title     :clients}]})
