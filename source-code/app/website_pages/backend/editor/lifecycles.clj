
(ns app.website-pages.backend.editor.lifecycles
    (:require [engines.item-editor.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-editor/init-editor! :website-pages.editor
                                              {:base-route      "/@app-home/website-pages"
                                               :collection-name "website_pages"
                                               :handler-key     :website-pages.editor
                                               :item-namespace  :page
                                               :on-route        [:website-pages.editor/load-editor!]
                                               :route-title     :website-pages}]})
