
(ns app.website-menus.backend.editor.lifecycles
    (:require [engines.item-editor.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-editor/init-editor! :website-menus.editor
                                              {:base-route      "/@app-home/website-menus"
                                               :collection-name "website_menus"
                                               :handler-key     :website-menus.editor
                                               :item-namespace  :menu
                                               :on-route        [:website-menus.editor/load-editor!]
                                               :route-title     :website-menus}]})
