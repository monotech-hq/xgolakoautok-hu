
(ns app.components.frontend.api
    (:require [app.components.frontend.consent-dialog.effects]
              [app.components.frontend.context-menu.effects]
              [app.components.frontend.action-bar.views               :as action-bar.views]
              [app.components.frontend.color-picker.views             :as color-picker.views]
              [app.components.frontend.data-element.views             :as data-element.views]
              [app.components.frontend.data-table.views               :as data-table.views]
              [app.components.frontend.error-content.views            :as error-content.views]
              [app.components.frontend.error-label.views              :as error-label.views]
              [app.components.frontend.ghost-view.views               :as ghost-view.views]
              [app.components.frontend.item-controls.views            :as item-controls.views]
              [app.components.frontend.item-info.views                :as item-info.views]
              [app.components.frontend.item-list-header.views         :as item-list-header.views]
              [app.components.frontend.item-list-row.views            :as item-list-row.views]
              [app.components.frontend.item-list-table.views          :as item-list-table.views]
              [app.components.frontend.list-item-avatar.views         :as list-item-avatar.views]
              [app.components.frontend.list-item-button.views         :as list-item-button.views]
              [app.components.frontend.list-item-cell.views           :as list-item-cell.views]
              [app.components.frontend.list-item-drag-handle.views    :as list-item-drag-handle.views]
              [app.components.frontend.list-item-gap.views            :as list-item-gap.views]
              [app.components.frontend.list-item-marker.views         :as list-item-marker.views]
              [app.components.frontend.list-item-thumbnail.views      :as list-item-thumbnail.views]
              [app.components.frontend.pdf-preview.views              :as pdf-preview.views]
              [app.components.frontend.popup-label-bar.views          :as popup-label-bar.views]
              [app.components.frontend.popup-menu-header.views        :as popup-menu-header.views]
              [app.components.frontend.popup-progress-indicator.views :as popup-progress-indicator.views]
              [app.components.frontend.surface-breadcrumbs.views      :as surface-breadcrumbs.views]
              [app.components.frontend.surface-box.views              :as surface-box.views]
              [app.components.frontend.surface-button.views           :as surface-button.views]
              [app.components.frontend.surface-description.views      :as surface-description.views]
              [app.components.frontend.surface-label.views            :as surface-label.views]
              [app.components.frontend.user-avatar.views              :as user-avatar.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app.components.frontend.*.views
(def action-bar               action-bar.views/component)
(def color-picker             color-picker.views/component)
(def data-element             data-element.views/component)
(def data-table               data-table.views/component)
(def error-content            error-content.views/component)
(def error-label              error-label.views/component)
(def ghost-view               ghost-view.views/component)
(def item-controls            item-controls.views/component)
(def item-info                item-info.views/component)
(def item-list-header         item-list-header.views/component)
(def item-list-row            item-list-row.views/component)
(def item-list-table          item-list-table.views/component)
(def list-item-avatar         list-item-avatar.views/component)
(def list-item-button         list-item-button.views/component)
(def list-item-cell           list-item-cell.views/component)
(def list-item-drag-handle    list-item-drag-handle.views/component)
(def list-item-gap            list-item-gap.views/component)
(def list-item-marker         list-item-marker.views/component)
(def list-item-thumbnail      list-item-thumbnail.views/component)
(def pdf-preview              pdf-preview.views/component)
(def popup-label-bar          popup-label-bar.views/component)
(def popup-menu-header        popup-menu-header.views/component)
(def popup-progress-indicator popup-progress-indicator.views/component)
(def surface-breadcrumbs      surface-breadcrumbs.views/component)
(def surface-box              surface-box.views/component)
(def surface-button           surface-button.views/component)
(def surface-description      surface-description.views/component)
(def surface-label            surface-label.views/component)
(def user-avatar              user-avatar.views/component)
