
(ns app.categories.backend.editor.lifecycles
    (:require [engines.item-editor.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-editor/init-editor! :categories.editor
                                              {:base-route      "/@app-home/categories"
                                               :collection-name "categories"
                                               :handler-key     :categories.editor
                                               :item-namespace  :category
                                               :on-route        [:categories.editor/load-editor!]
                                               :route-title     :categories}]})
