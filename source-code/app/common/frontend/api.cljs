
(ns app.common.frontend.api
    (:require [app.common.frontend.item-picker.subs]
              [app.common.frontend.item-selector.effects]
              [app.common.frontend.item-selector.events]
              [app.common.frontend.file-editor.views           :as file-editor.views]
              [app.common.frontend.item-browser.views          :as item-browser.views]
              [app.common.frontend.item-editor.views           :as item-editor.views]
              [app.common.frontend.item-lister.views           :as item-lister.views]
              [app.common.frontend.item-preview.views          :as item-preview.views]
              [app.common.frontend.item-picker.views           :as item-picker.views]
              [app.common.frontend.item-selector.subs          :as item-selector.subs]
              [app.common.frontend.item-selector.views         :as item-selector.views]
              [app.common.frontend.item-viewer.views           :as item-viewer.views]
              [app.common.frontend.selector-item-counter.views :as selector-item-counter.views]
              [app.common.frontend.selector-item-marker.views  :as selector-item-marker.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app.common.frontend.file-editor.views
(def file-editor-header file-editor.views/file-editor-header)
(def file-editor-body   file-editor.views/file-editor-body)

; app.common.frontend.item-browser.views
(def item-browser-footer item-browser.views/item-browser-footer)
(def item-browser-header item-browser.views/item-browser-header)
(def item-browser-body   item-browser.views/item-browser-body)

; app.common.frontend.item-editor.views
(def item-editor-footer item-editor.views/item-editor-footer)
(def item-editor-header item-editor.views/item-editor-header)
(def item-editor-body   item-editor.views/item-editor-body)

; app.common.frontend.item-lister.views
(def item-lister-footer item-lister.views/item-lister-footer)
(def item-lister-header item-lister.views/item-lister-header)
(def item-lister-body   item-lister.views/item-lister-body)

; app.common.frontend.item-picker.views
(def item-picker item-picker.views/element)

; app.common.frontend.item-preview.views
(def item-preview item-preview.views/element)

; app.common.frontend.item-selector.views
(def item-selector-footer      item-selector.views/item-selector-footer)
(def item-selector-control-bar item-selector.views/item-selector-control-bar)
(def item-selector-label-bar   item-selector.views/item-selector-label-bar)
(def item-selector-body        item-selector.views/item-selector-body)

; app.common.frontend.item-viewer.views
(def item-viewer-footer item-viewer.views/item-viewer-footer)
(def item-viewer-header item-viewer.views/item-viewer-header)
(def item-viewer-body   item-viewer.views/item-viewer-body)

; app.common.frontend.selector-item-counter.views
(def selector-item-counter selector-item-counter.views/element)

; app.common.frontend.selector-item-marker.views
(def selector-item-marker selector-item-marker.views/element)
