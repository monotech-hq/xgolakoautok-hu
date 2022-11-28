
(ns app.website-menus.frontend.api
    (:require [app.website-menus.frontend.editor.effects]
              [app.website-menus.frontend.editor.views]
              [app.website-menus.frontend.lifecycles]
              [app.website-menus.frontend.lister.effects]
              [app.website-menus.frontend.lister.lifecycles]
              [app.website-menus.frontend.lister.views]
              [app.website-menus.frontend.selector.effects]
              [app.website-menus.frontend.viewer.effects]
              [app.website-menus.frontend.viewer.views]
              [app.website-menus.frontend.picker.views  :as picker.views]
              [app.website-menus.frontend.preview.views :as preview.views]))
 
;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app.website-menus.frontend.picker.views
(def menu-picker picker.views/element)

; app.website-menus.frontend.preview.views
(def menu-preview preview.views/element)
