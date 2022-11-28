
(ns app.website-menus.backend.selector.lifecycles
    (:require [engines.item-lister.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-lister/init-lister! :website-menus.selector
                                              {:collection-name "website_menus"
                                               :handler-key     :website-menus.lister
                                               :item-namespace  :menu}]})
