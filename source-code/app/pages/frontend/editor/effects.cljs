
(ns app.pages.frontend.editor.effects
    (:require [app.pages.frontend.editor.views :as editor.views]
              [re-frame.api                    :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pages.editor/load-editor!
  {:dispatch-n [[:gestures/init-view-handler! :pages.editor
                                              {:default-view-id :data}]
                [:ui/render-surface! :pages.editor/view
                                     {:page #'editor.views/view}]]})
