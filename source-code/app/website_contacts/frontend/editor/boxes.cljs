
(ns app.website-contacts.frontend.editor.boxes
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [app.contents.frontend.api   :as contents]
              [elements.api                :as elements]
              [forms.api                   :as forms]
              [re-frame.api                :as r]
              [vector.api                  :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- address-data-information-picker
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-contacts.editor])]
       [contents/content-picker ::address-data-information-picker
                                {:autosave?     true
                                 :color         :muted
                                 :disabled?     editor-disabled?
                                 :indent        {:vertical :s}
                                 :multi-select? false
                                 :placeholder   "n/a"
                                 :value-path    [:website-contacts :editor/edited-item :address-data-information]}]))

(defn- address-data-information-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-contacts.editor])]
       [components/surface-box ::address-data-information-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [address-data-information-picker]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :indent    {:top :m}
                                :label     :address-data-information}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- contacts-data-information-picker
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-contacts.editor])]
       [contents/content-picker ::contacts-data-information-picker
                                {:autosave?     true
                                 :color         :muted
                                 :disabled?     editor-disabled?
                                 :indent        {:vertical :s}
                                 :multi-select? false
                                 :placeholder   "n/a"
                                 :value-path    [:website-contacts :editor/edited-item :contacts-data-information]}]))

(defn- contacts-data-information-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-contacts.editor])]
       [components/surface-box ::contacts-data-information-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [contacts-data-information-picker]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :indent    {:top :m}
                                :label     :contacts-data-information}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- duplicate-contact-group-button
  [group-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-contacts.editor])]
       [elements/button {:color     :primary
                         :disabled? editor-disabled?
                         :font-size :xs
                         :indent    {:right :s :top :m}
                         :label     :duplicate!
                         :on-click  [:x.db/apply-item! [:website-contacts :editor/edited-item :contact-groups]
                                                       vector/duplicate-nth-item group-dex]}]))

(defn- delete-contact-group-button
  [group-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-contacts.editor])]
       [elements/button {:color     :warning
                         :disabled? editor-disabled?
                         :font-size :xs
                         :indent    {:right :s :top :m}
                         :label     :delete!
                         :on-click  [:x.db/apply-item! [:website-contacts :editor/edited-item :contact-groups]
                                                       vector/remove-nth-item group-dex]}]))

(defn- contact-group-label-field
  [group-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-contacts.editor])]
       [elements/text-field {:disabled?   editor-disabled?
                             :label       :label
                             :indent      {:top :m :vertical :s}
                             :placeholder :contacts-label-placeholder
                             :value-path  [:website-contacts :editor/edited-item :contact-groups group-dex :label]}]))

(defn- email-addresses-field
  [group-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-contacts.editor])]
       [elements/multi-field {:disabled?   editor-disabled?
                              :label       :email-address
                              :indent      {:top :m :vertical :s}
                              :placeholder :email-address-placeholder
                              :value-path  [:website-contacts :editor/edited-item :contact-groups group-dex :email-addresses]}]))

(defn- phone-numbers-field
  [group-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-contacts.editor])]
       [elements/multi-field {:disabled?   editor-disabled?
                              :label       :phone-number
                              :indent      {:top :m :vertical :s}
                              :placeholder :phone-number-placeholder
                              :value-path  [:website-contacts :editor/edited-item :contact-groups group-dex :phone-numbers]}]))

(defn- contact-group-box
  [group-dex group-props]
  [components/surface-box {:content [:<> [:div (forms/form-row-attributes)
                                               [:div (forms/form-block-attributes {:ratio 100})
                                                     [contact-group-label-field group-dex group-props]]
                                          [:div (forms/form-row-attributes)
                                               [:div (forms/form-block-attributes {:ratio 50})
                                                     [phone-numbers-field group-dex group-props]]
                                               [:div (forms/form-block-attributes {:ratio 50})
                                                     [email-addresses-field group-dex group-props]]]]
                                         [:div {:style {:display :flex :justify-content :flex-end}}
                                               [duplicate-contact-group-button group-dex group-props]
                                               [delete-contact-group-button    group-dex group-props]]
                                         [elements/horizontal-separator {:height :xs}]]
                           :indent {:top :m}}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- contact-group-list
  []
  (letfn [(f [%1 %2 %3] (conj %1 [contact-group-box %2 %3]))]
         (let [contact-groups @(r/subscribe [:x.db/get-item [:website-contacts :editor/edited-item :contact-groups]])]
              (reduce-kv f [:<>] contact-groups))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- contacts-controls-action-bar
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-contacts.editor])
        on-click [:x.db/apply-item! [:website-contacts :editor/edited-item :contact-groups] vector/cons-item {}]]
       [components/action-bar ::contacts-controls-action-bar
                              {:disabled? editor-disabled?
                               :label     :add-contacts-data!
                               :on-click  on-click}]))

(defn- contacts-data-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-contacts.editor])]
       [components/surface-box ::contacts-data-box
                               {:content [:<> [contacts-controls-action-bar]
                                              [elements/horizontal-separator {:height :xs}]]
                                :disabled? editor-disabled?
                                :label     :contacts-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- duplicate-address-group-button
  [group-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-contacts.editor])]
       [elements/button {:color     :primary
                         :disabled? editor-disabled?
                         :font-size :xs
                         :indent    {:right :s :top :m}
                         :label     :duplicate!
                         :on-click  [:x.db/apply-item! [:website-contacts :editor/edited-item :address-groups]
                                                       vector/duplicate-nth-item group-dex]}]))

(defn- delete-address-group-button
  [group-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-contacts.editor])]
       [elements/button {:color     :warning
                         :disabled? editor-disabled?
                         :font-size :xs
                         :indent    {:right :s :top :m}
                         :label     :delete!
                         :on-click  [:x.db/apply-item! [:website-contacts :editor/edited-item :address-groups]
                                                       vector/remove-nth-item group-dex]}]))

(defn- address-group-label-field
  [group-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-contacts.editor])]
       [elements/text-field {:disabled?   editor-disabled?
                             :label       :label
                             :indent      {:top :m :vertical :s}
                             :placeholder :address-label-placeholder
                             :value-path  [:website-contacts :editor/edited-item :address-groups group-dex :label]}]))

(defn- company-address-field
  [group-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-contacts.editor])]
       [elements/text-field {:disabled?   editor-disabled?
                             :label       :address
                             :indent      {:top :m :vertical :s}
                             :placeholder :full-address-placeholder
                             :value-path  [:website-contacts :editor/edited-item :address-groups group-dex :company-address]}]))

(defn- google-maps-link
  [group-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-contacts.editor])
        company-address  @(r/subscribe [:x.db/get-item [:website-contacts :editor/edited-item :address-groups group-dex :company-address]])]
       [components/data-element {:disabled? editor-disabled?
                                 :label     :google-maps-link
                                 :value     (str "https://www.google.com/maps/search/?api=1&query=" company-address)
                                 :indent    {:top :xxl :vertical :s}}]))

(defn- google-maps-link-toggle
  [group-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-contacts.editor])
        company-address  @(r/subscribe [:x.db/get-item [:website-contacts :editor/edited-item :address-groups group-dex :company-address]])
        company-address-link (str "https://www.google.com/maps/search/?api=1&query=" company-address)]
       [:div {:style {:display :flex}}
             [elements/button {:color     :primary
                               :disabled? editor-disabled?
                               :font-size :xs
                               :label     :open-link!
                               :on-click  {:fx [:x.environment/open-tab! company-address-link]}
                               :indent    {:vertical :s}}]]))

(defn- address-group-box
  [group-dex group-props]
  [components/surface-box {:content [:<> [:div (forms/form-row-attributes)
                                               [:div (forms/form-block-attributes {:ratio 100})
                                                     [address-group-label-field group-dex group-props]]
                                          [:div (forms/form-row-attributes)
                                               [:div (forms/form-block-attributes {:ratio 100})
                                                     [company-address-field group-dex group-props]]]]
                                         [:div (forms/form-row-attributes)
                                               [:div (forms/form-block-attributes {:ratio 100})
                                                     [google-maps-link        group-dex group-props]
                                                     [google-maps-link-toggle group-dex group-props]]]
                                         [:div {:style {:display :flex :justify-content :flex-end}}
                                               [duplicate-address-group-button group-dex group-props]
                                               [delete-address-group-button    group-dex group-props]]
                                         [elements/horizontal-separator {:height :xs}]]
                           :indent  {:top :m}}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- address-group-list
  []
  (letfn [(f [%1 %2 %3] (conj %1 [address-group-box %2 %3]))]
         (let [address-groups @(r/subscribe [:x.db/get-item [:website-contacts :editor/edited-item :address-groups]])]
              (reduce-kv f [:<>] address-groups))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- address-controls-action-bar
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-contacts.editor])
        on-click [:x.db/apply-item! [:website-contacts :editor/edited-item :address-groups] vector/cons-item {}]]
       [components/action-bar ::address-controls-action-bar
                              {:disabled? editor-disabled?
                               :label     :add-address-data!
                               :on-click  on-click}]))

(defn- address-data-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-contacts.editor])]
       [components/surface-box ::address-data-box
                               {:content [:<> [address-controls-action-bar]
                                              [elements/horizontal-separator {:height :xs}]]
                                :disabled? editor-disabled?
                                :label     :address-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- facebook-links-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-contacts.editor])]
       [elements/multi-field ::facebook-links-field
                             {:disabled?   editor-disabled?
                              :indent      {:top :m :vertical :s}
                              :label       :facebook-link
                              :placeholder :facebook-link-placeholder
                              :value-path  [:website-contacts :editor/edited-item :facebook-links]}]))

(defn- instagram-links-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-contacts.editor])]
       [elements/multi-field ::instagram-links-field
                             {:disabled?   editor-disabled?
                              :indent      {:top :m :vertical :s}
                              :label       :instagram-link
                              :placeholder :instagram-link-placeholder
                              :value-path  [:website-contacts :editor/edited-item :instagram-links]}]))

(defn- youtube-links-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-contacts.editor])]
       [elements/multi-field ::youtube-links-field
                             {:disabled?   editor-disabled?
                              :indent      {:top :m :vertical :s}
                              :label       :youtube-link
                              :placeholder :youtube-link-placeholder
                              :value-path  [:website-contacts :editor/edited-item :youtube-links]}]))

(defn- social-media-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-contacts.editor])]
       [components/surface-box ::social-media-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [facebook-links-field]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [instagram-links-field]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [youtube-links-field]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? editor-disabled?
                                :label     :social-media}]))
