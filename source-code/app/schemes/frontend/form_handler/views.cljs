
(ns app.schemes.frontend.form-handler.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [re-frame.api            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-field-bar
  ; @param (keyword) scheme-id
  ; @param (keyword) editor-id
  ; @param (map) bar-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)}
  [scheme-id _ {:keys [disabled? indent]}]
  (let [on-click [:schemes.field-editor/load-editor! scheme-id]]
       [common/action-bar ::add-field-bar
                          {:disabled? disabled?
                           :indent    indent
                           :label     :add-field!
                           :on-click  on-click}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn scheme-field-handle-icon-button
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  ; @param (map) block-props
  [_ _ _]
  [elements/icon-button {:color  :muted
                         :disabled? true
                         :height :l
                         :icon   :drag_handle}])

(defn scheme-field
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  ; @param (map) block-props
  ;  {:autofocus? (boolean)(opt)
  ;   :disabled? (boolean)(opt)
  ;   :options-path (vector)(opt)
  ;   :value-path (vector)}
  [scheme-id field-id {:keys [autofocus? disabled? options-path value-path]}]
  (let [{:field/keys [field-id field-no name unit] :as field-props} @(r/subscribe [:schemes.form-handler/get-scheme-field scheme-id field-id])]
       [elements/combo-box {:autofocus?     autofocus?
                            :disabled?      (or disabled? (:field/disabled? field-props))
                            :emptiable?     false
                            :end-adornments [{:label unit}
                                             {:icon :more_vert :on-click [:schemes.field-menu/render-menu! scheme-id field-id]}]
                            :label          name
                            :indent         {:top :m :right :s}
                            :info-text      {:content :field-id-n :replacements [field-no]}
                            :options-path   options-path
                            :value-path     value-path}]))

(defn scheme-field-block
  ; @param (keyword) scheme-id
  ; @param (keyword) field-id
  ; @param (map) block-props
  ;  {:autofocus? (boolean)(opt)
  ;   :disabled? (boolean)(opt)
  ;   :value-path (vector)}
  [scheme-id field-id block-props]
  [:div {:style {:display :flex}}
        [:div {:style {:align-items :flex-end :display :flex}}
              [scheme-field-handle-icon-button scheme-id field-id block-props]]
        [:div {:style {:flex-grow 1}}
              [scheme-field scheme-id field-id block-props]]])
