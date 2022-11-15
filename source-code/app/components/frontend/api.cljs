
(ns app.components.frontend.api
    (:require [app.components.frontend.consent-dialog.effects]
              [app.components.frontend.context-menu.effects]
              [app.components.frontend.item-controls.views         :as item-controls.views]
              [app.components.frontend.list-header.views           :as list-header.views]
              [app.components.frontend.list-item-cell.views        :as list-item-cell.views]
              [app.components.frontend.list-item-drag-handle.views :as list-item-drag-handle.views]
              [app.components.frontend.list-item-marker.views      :as list-item-marker.views]
              [app.components.frontend.list-item-row.views         :as list-item-row.views]
              [app.components.frontend.list-item-thumbnail.views   :as list-item-thumbnail.views]
              [app.components.frontend.popup-label-bar.views       :as popup-label-bar.views]
              [app.components.frontend.surface-breadcrumbs.views   :as surface-breadcrumbs.views]
              [app.components.frontend.surface-description.views   :as surface-description.views]
              [app.components.frontend.surface-button.views        :as surface-button.views]
              [app.components.frontend.surface-label.views         :as surface-label.views]
              [app.components.frontend.user-avatar.views           :as user-avatar.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app.components.frontend.*.views
(def item-controls         item-controls.views/component)
(def list-header           list-header.views/component)
(def list-item-cell        list-item-cell.views/component)
(def list-item-drag-handle list-item-drag-handle.views/component)
(def list-item-marker      list-item-marker.views/component)
(def list-item-row         list-item-row.views/component)
(def list-item-thumbnail   list-item-thumbnail.views/component)
(def popup-label-bar       popup-label-bar.views/component)
(def surface-breadcrumbs   surface-breadcrumbs.views/component)
(def surface-description   surface-description.views/component)
(def surface-button        surface-button.views/component)
(def surface-label         surface-label.views/component)
(def user-avatar           user-avatar.views/component)
