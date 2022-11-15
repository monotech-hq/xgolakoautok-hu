
(ns app.packages.backend.editor.lifecycles
    (:require [engines.item-editor.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:item-editor/init-editor! :packages.editor
                                              {:base-route      "/@app-home/packages"
                                               :collection-name "packages"
                                               :handler-key     :packages.editor
                                               :item-namespace  :package
                                               :on-route        [:packages.editor/load-editor!]
                                               :route-title     :packages}]})
