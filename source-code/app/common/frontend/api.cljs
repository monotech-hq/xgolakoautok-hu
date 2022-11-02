
(ns app.common.frontend.api
    (:require [app.common.frontend.item-selector.effects]
              [app.common.frontend.item-selector.events]
              [app.common.frontend.action-bar.views            :as action-bar.views]
              [app.common.frontend.data-element.views          :as data-element.views]
              [app.common.frontend.data-table.views            :as data-table.views]
              [app.common.frontend.error-content.views         :as error-content.views]
              [app.common.frontend.error-element.views         :as error-element.views]
              [app.common.frontend.file-editor.views           :as file-editor.views]
              [app.common.frontend.ghost-element.views         :as ghost-element.views]
              [app.common.frontend.item-browser.views          :as item-browser.views]
              [app.common.frontend.item-editor.views           :as item-editor.views]
              [app.common.frontend.item-list.views             :as item-list.views]
              [app.common.frontend.item-lister.views           :as item-lister.views]
              [app.common.frontend.item-preview.views          :as item-preview.views]
              [app.common.frontend.item-selector.subs          :as item-selector.subs]
              [app.common.frontend.item-selector.views         :as item-selector.views]
              [app.common.frontend.item-viewer.views           :as item-viewer.views]
              [app.common.frontend.list-item-drag-handle.views :as list-item-drag-handle.views]
              [app.common.frontend.list-item-marker.views      :as list-item-marker.views]
              [app.common.frontend.menu-header.views           :as menu-header.views]
              [app.common.frontend.popup.views                 :as popup.views]
              [app.common.frontend.pdf-preview.views           :as pdf-preview.views]
              [app.common.frontend.selector-item-counter.views :as selector-item-counter.views]
              [app.common.frontend.selector-item-marker.views  :as selector-item-marker.views]
              [app.common.frontend.surface.views               :as surface.views]
              [app.common.frontend.surface-box.views           :as surface-box.views]
              [app.common.frontend.surface-button.views        :as surface-button.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app.common.frontend.action-bar.views
(def action-bar action-bar.views/element)

; app.common.frontend.data-element.views
(def data-element data-element.views/element)

; app.common.frontend.data-table.views
(def data-table data-table.views/element)

; app.common.frontend.error-content.views
(def error-content error-content.views/element)

; app.common.frontend.error-element.views
(def error-element error-element.views/element)

; app.common.frontend.file-editor.views
(def file-editor-menu-bar      file-editor.views/file-editor-menu-bar)
(def file-editor-ghost-element file-editor.views/file-editor-ghost-element)
(def file-editor-controls      file-editor.views/file-editor-controls)

; app.common.frontend.ghost-element.views
(def ghost-element ghost-element.views/element)

; app.common.frontend.item-browser.views
(def item-browser-search-field       item-browser.views/item-browser-search-field)
(def item-browser-search-description item-browser.views/item-browser-search-description)

; app.common.frontend.item-editor.views
(def item-editor-color-picker  item-editor.views/item-editor-color-picker)
(def item-editor-menu-bar      item-editor.views/item-editor-menu-bar)
(def item-editor-ghost-element item-editor.views/item-editor-ghost-element)
(def item-editor-controls      item-editor.views/item-editor-controls)

; app.common.frontend.item-list.views
(def item-list item-list.views/element)

; app.common.frontend.item-lister.views
(def list-item-structure            item-lister.views/list-item-structure)
(def list-item-icon-button          item-lister.views/list-item-icon-button)
(def list-item-thumbnail            item-lister.views/list-item-thumbnail)
(def list-item-thumbnail-icon       item-lister.views/list-item-thumbnail-icon)
(def list-item-label                item-lister.views/list-item-label)
(def list-item-details              item-lister.views/list-item-details)
(def list-item-detail               item-lister.views/list-item-detail)
(def list-item-primary-cell         item-lister.views/list-item-primary-cell)
(def item-lister-search-field       item-lister.views/item-lister-search-field)
(def item-lister-search-description item-lister.views/item-lister-search-description)
(def item-lister-header-spacer      item-lister.views/item-lister-header-spacer)
(def item-lister-header-cell        item-lister.views/item-lister-header-cell)
(def item-lister-header             item-lister.views/item-lister-header)
(def item-lister-wrapper            item-lister.views/item-lister-wrapper)
(def item-lister-ghost-element      item-lister.views/item-lister-ghost-element)
(def item-lister-ghost-header       item-lister.views/item-lister-ghost-header)
(def item-lister-create-item-button item-lister.views/item-lister-create-item-button)
(def item-lister-download-info      item-lister.views/item-lister-download-info)

; app.common.frontend.item-preview.views
(def item-preview-ghost-element item-preview.views/item-preview-ghost-element)

; app.common.frontend.item-selector.views
(def item-selector-footer        item-selector.views/item-selector-footer)
(def item-selector-control-bar   item-selector.views/item-selector-control-bar)
(def item-selector-ghost-element item-selector.views/item-selector-ghost-element)

; app.common.frontend.item-viewer.views
(def item-viewer-item-info     item-viewer.views/item-viewer-item-info)
(def item-viewer-color-stamp   item-viewer.views/item-viewer-color-stamp)
(def item-viewer-menu-bar      item-viewer.views/item-viewer-menu-bar)
(def item-viewer-ghost-element item-viewer.views/item-viewer-ghost-element)
(def item-viewer-controls      item-viewer.views/item-viewer-controls)

; app.common.frontend.list-item-drag-handle.views
(def list-item-drag-handle list-item-drag-handle.views/element)

; app.common.frontend.list-item-marker.views
(def list-item-marker list-item-marker.views/element)

; app.common.frontend.selector-item-counter.views
(def selector-item-counter selector-item-counter.views/element)

; app.common.frontend.selector-item-marker.views
(def selector-item-marker selector-item-marker.views/element)

; app.common.frontend.menu-header.views
(def menu-header menu-header.views/element)

; app.common.frontend.popup.views
(def popup-label-bar          popup.views/popup-label-bar)
(def popup-progress-indicator popup.views/popup-progress-indicator)

; app.common.frontend.pdf-preview.views
(def pdf-preview pdf-preview.views/element)

; app.common.frontend.surface.views
(def go-back-button                surface.views/go-back-button)
(def surface-label                 surface.views/surface-label)
(def surface-description           surface.views/surface-description)
(def surface-breadcrumbs           surface.views/surface-breadcrumbs)
(def surface-box-layout-ghost-view surface.views/surface-box-layout-ghost-view)

; app.common.frontend.surface-box.views
(def surface-box surface-box.views/element)

; app.common.frontend.surface-button.views
(def surface-button surface-button.views/element)
