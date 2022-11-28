
(ns app.website-menus.backend.viewer.lifecycles
    (:require [engines.item-viewer.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-viewer/init-viewer! :website-menus.viewer
                                              {:base-route      "/@app-home/website-menus"
                                               :collection-name "website_menus"
                                               :handler-key     :website-menus.viewer
                                               :item-namespace  :menu
                                               :on-route        [:website-menus.viewer/load-viewer!]
                                               :route-title     :website-menus}]})
