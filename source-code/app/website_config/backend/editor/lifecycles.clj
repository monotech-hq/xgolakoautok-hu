
(ns app.website-config.backend.editor.lifecycles
    (:require [engines.file-editor.api]
              [x.server-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:file-editor/init-editor! :website-config.editor
                                              {:base-route  "/@app-home/website-config"
                                               :handler-key :website-config.editor
                                               :on-route    [:website-config.editor/load-editor!]
                                               :route-title :website-config}]})
