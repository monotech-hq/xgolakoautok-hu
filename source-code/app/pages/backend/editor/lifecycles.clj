
(ns app.pages.backend.editor.lifecycles
    (:require [engines.item-editor.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-editor/init-editor! :pages.editor
                                              {:base-route      "/@app-home/pages"
                                               :collection-name "pages"
                                               :handler-key     :pages.editor
                                               :item-namespace  :page
                                               :on-route        [:pages.editor/load-editor!]
                                               :route-title     :pages}]})
