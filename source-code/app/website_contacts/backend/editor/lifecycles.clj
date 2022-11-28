
(ns app.website-contacts.backend.editor.lifecycles
    (:require [engines.file-editor.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot [:file-editor/init-editor! :website-contacts.editor
                                              {:base-route  "/@app-home/website-contacts"
                                               :handler-key :website-contacts.editor
                                               :on-route    [:website-contacts.editor/load-editor!]
                                               :route-title :website-contacts}]})
