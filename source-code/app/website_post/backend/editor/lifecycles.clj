
(ns app.website-post.backend.editor.lifecycles
    (:require [engines.file-editor.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:file-editor/init-editor! :website-post.editor
                                              {:base-route  "/@app-home/website-post"
                                               :handler-key :website-post.editor
                                               :on-route    [:website-post.editor/load-editor!]
                                               :route-title :website-post}]})
