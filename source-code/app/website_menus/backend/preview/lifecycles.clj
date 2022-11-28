
(ns app.website-menus.backend.preview.lifecycles
    (:require [engines.item-preview.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-preview/init-preview! :website-menus.preview
                                                {:collection-name "website_menus"
                                                 :handler-key     :website-menus.preview
                                                 :item-namespace  :menu}]})
