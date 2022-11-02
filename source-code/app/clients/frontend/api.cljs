
(ns app.clients.frontend.api
    (:require [app.clients.frontend.editor.effects]
              [app.clients.frontend.editor.subs]
              [app.clients.frontend.editor.views]
              [app.clients.frontend.lifecycles]
              [app.clients.frontend.lister.effects]
              [app.clients.frontend.lister.lifecycles]
              [app.clients.frontend.lister.subs]
              [app.clients.frontend.lister.views]
              [app.clients.frontend.preview.subs]
              [app.clients.frontend.selector.effects]
              [app.clients.frontend.selector.subs]
              [app.clients.frontend.viewer.effects]
              [app.clients.frontend.viewer.subs]
              [app.clients.frontend.viewer.views]
              [app.clients.frontend.picker.views  :as picker.views]
              [app.clients.frontend.preview.views :as preview.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app.clients.frontend.picker.views
(def client-picker picker.views/element)

; app.clients.frontend.preview.views
(def client-preview preview.views/element)
