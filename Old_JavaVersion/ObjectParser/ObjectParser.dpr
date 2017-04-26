program ObjectParser;

uses
  Forms,
  Main in 'Main.pas' {MainForm};

{$R *.res}

begin
  Application.Initialize;
  Application.Title := 'DCI Object Parser';
  Application.CreateForm(TMainForm, MainForm);
  Application.Run;
end.
