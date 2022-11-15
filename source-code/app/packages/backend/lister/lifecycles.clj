
(ns app.packages.backend.lister.lifecycles
    (:require [engines.item-lister.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :packages.lister
                                              {:base-route      "/@app-home/packages"
                                               :collection-name "packages"
                                               :handler-key     :packages.lister
                                               :item-namespace  :package
                                               :on-route        [:packages.lister/load-lister!]
                                               :route-title     :packages}]})
