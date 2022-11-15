
(ns app.contents.backend.editor.lifecycles
    (:require [engines.item-editor.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-editor/init-editor! :contents.editor
                                              {:base-route      "/@app-home/contents"
                                               :collection-name "contents"
                                               :handler-key     :contents.editor
                                               :item-namespace  :content
                                               :on-route        [:contents.editor/load-editor!]
                                               :route-title     :contents}]})
