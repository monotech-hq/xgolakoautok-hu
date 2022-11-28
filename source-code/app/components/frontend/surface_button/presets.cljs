
(ns app.components.frontend.surface-button.presets)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BUTTON-PROPS-PRESETS
     {:primary   {:background-color "#5a4aff"
                  :color            "#ffffff"
                  :hover-color      "#6a5aff"}
      :secondary {:hover-color      :highlight}
      :add       {:background-color "#5a4aff"
                  :color            "#ffffff"
                  :hover-color      "#6a5aff"
                  :icon             :add
                  :label            :add!}
      :delete    {:color            :warning
                  :hover-color      :highlight
                  :icon             :delete_outline
                  :label            :delete!}
      :duplicate {:hover-color      :highlight
                  :icon             :file_copy
                  :icon-family      :material-icons-outlined
                  :label            :duplicate!}
      :edit      {:background-color "#5a4aff"
                  :color            "#ffffff"
                  :hover-color      "#6a5aff"
                  :icon             :edit
                  :label            :edit!}
      :revert    {:hover-color      :highlight
                  :icon             :settings_backup_restore
                  :label            :revert!}
      :save      {:background-color "#5a4aff"
                  :color            "#ffffff"
                  :hover-color      "#6a5aff"
                  :icon             :save
                  :label            :save!}})
