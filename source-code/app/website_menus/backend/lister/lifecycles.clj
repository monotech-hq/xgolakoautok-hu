
(ns app.website-menus.backend.lister.lifecycles
    (:require [engines.item-lister.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :website-menus.lister
                                              {:base-route      "/@app-home/website-menus"
                                               :collection-name "website_menus"
                                               :handler-key     :website-menus.lister
                                               :item-namespace  :menu
                                               :on-route        [:website-menus.lister/load-lister!]
                                               :route-title     :website-menus}]})
