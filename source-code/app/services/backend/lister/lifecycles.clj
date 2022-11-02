
(ns app.services.backend.lister.lifecycles
    (:require [engines.item-lister.api]
              [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :services.lister
                                              {:base-route      "/@app-home/services"
                                               :collection-name "services"
                                               :handler-key     :services.lister
                                               :item-namespace  :service
                                               :on-route        [:services.lister/load-lister!]
                                               :route-title     :services}]})
