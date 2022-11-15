
(ns app.website-content.backend.editor.lifecycles
    (:require [engines.file-editor.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:file-editor/init-editor! :website-content.editor
                                              {:base-route  "/@app-home/website-content"
                                               :handler-key :website-content.editor
                                               :on-route    [:website-content.editor/load-editor!]
                                               :route-title :website-content}]})
